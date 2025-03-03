/*
 * Copyright (c) 2009--2012 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package com.redhat.rhn.manager.kickstart.tree;

import com.redhat.rhn.common.hibernate.HibernateFactory;
import com.redhat.rhn.common.validator.ValidatorError;
import com.redhat.rhn.common.validator.ValidatorException;
import com.redhat.rhn.domain.channel.Channel;
import com.redhat.rhn.domain.channel.ChannelFactory;
import com.redhat.rhn.domain.kickstart.KickstartData;
import com.redhat.rhn.domain.kickstart.KickstartFactory;
import com.redhat.rhn.domain.kickstart.KickstartInstallType;
import com.redhat.rhn.domain.kickstart.KickstartableTree;
import com.redhat.rhn.domain.user.User;
import com.redhat.rhn.frontend.dto.PackageListItem;
import com.redhat.rhn.manager.BasePersistOperation;
import com.redhat.rhn.manager.kickstart.cobbler.CobblerCommand;
import com.redhat.rhn.manager.kickstart.cobbler.CobblerXMLRPCHelper;

import org.cobbler.Distro;
import org.cobbler.XmlRpcException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BaseTreeEditCommand
 */
public abstract class BaseTreeEditOperation extends BasePersistOperation {
    private static final String INVALID_INITRD = "kickstart.tree.invalidinitrd";
    private static final String INVALID_KERNEL = "kickstart.tree.invalidkernel";
    protected KickstartableTree tree;
    protected String serverName;
    private static final String EMPTY_STRING = "";
    public static final String KICKSTART_CAPABILITY = "rhn.kickstart.boot_image";

    /**
     * Constructor
     * @param userIn to associate with cmd.
     */
    protected BaseTreeEditOperation(User userIn) {
        this.user = userIn;
    }

    /**
     * Constructor for use when looking up by label
     * @param treeLabel to lookup
     * @param userIn who owns the tree
     */
    protected BaseTreeEditOperation(String treeLabel, User userIn) {
        this(userIn);
        this.tree = KickstartFactory.
            lookupKickstartTreeByLabel(treeLabel, userIn.getOrg());
    }

    /**
     * {@inheritDoc}
     */
    public ValidatorError store() {
        if (!this.validateLabel()) {
            HibernateFactory.getSession().evict(this.tree);
            return new ValidatorError("kickstart.tree.invalidlabel");
        }

        try {
            validateBasePath();
        }
        catch (ValidatorException ve) {
            return ve.getResult().getErrors().get(0);
        }

        KickstartFactory.saveKickstartableTree(this.tree);
        // Sync to cobbler
        try {
            CobblerCommand command = getCobblerCommand();
            ValidatorError error = command.store();

            if (error != null) {
                return error;
            }

            Distro distro = Distro.lookupById(CobblerXMLRPCHelper.getConnection(
                    this.getUser()), tree.getCobblerId());
            Distro xenDistro = Distro.lookupById(CobblerXMLRPCHelper.getConnection(
                    this.getUser()), tree.getCobblerXenId());

            if (distro == null) {
                return new ValidatorError("tree.edit.missingcobblerentry");
            }
            distro.setKernelOptions(getKernelOptions());
            distro.setKernelOptionsPost(getKernelOptionsPost());
            distro.save();
            if (xenDistro != null) {
                xenDistro.setKernelOptions(getKernelOptions());
                xenDistro.setKernelOptionsPost(getKernelOptionsPost());
                xenDistro.save();
            }
        }
        catch (XmlRpcException xe) {
            HibernateFactory.rollbackTransaction();
            if (xe.getCause().getMessage().contains("kernel not found")) {
                return new ValidatorError(INVALID_KERNEL,
                        this.tree.getKernelPath());
            }
            else if (xe.getCause().getMessage().contains("initrd not found")) {
                return new ValidatorError(INVALID_INITRD,
                        this.tree.getInitrdPath());
            }
            else {
                throw new RuntimeException(xe.getCause());
            }
        }
        catch (Exception e) {
            HibernateFactory.rollbackTransaction();
            String message = e.getMessage();
            if (message != null && message.contains("kernel not found")) {
                return new ValidatorError(INVALID_KERNEL,
                        this.tree.getKernelPath());
            }
            else if (message != null && message.contains("initrd not found")) {
                return new ValidatorError(INVALID_INITRD,
                        this.tree.getInitrdPath());
            }
            else {
                throw new RuntimeException(e);
            }

        }
        return null;
    }

    /**
     * Validate the label to make sure:
     *
     * "The Distribution Label field should contain only letters, numbers, hyphens,
     * periods, and underscores. It must also be at least 4 characters long."
     *
     * @return boolean if its valid or not
     */
    public boolean validateLabel() {
        String regEx = "^([-_0-9A-Za-z@.]{1,255})$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(this.getTree().getLabel());
        return matcher.matches();
    }

    /**
     * Ensures that the base path is correctly setup..
     * As in the initrd and kernel structures are setup correctly.
     * @throws ValidatorException if those paths don;t exist
     */
    public void validateBasePath() throws ValidatorException {
        getTree().getInitrdPath();
        getTree().getKernelPath();
    }

    /**
     * @return Returns the tree.
     */
    public KickstartableTree getTree() {
        return tree;
    }

    /**
     * Set the Install type for this Kickstart
     * @param typeIn to set on this KickstartableTree
     */
    public void setInstallType(KickstartInstallType typeIn) {
        this.tree.setInstallType(typeIn);
    }

    /**
     * Set the label on the tree
     * @param labelIn to set
     */
    public void setLabel(String labelIn) {
        this.tree.setLabel(labelIn);
    }

    /**
     * Set the local hostname of the installation server.
     * @param localHostname to set
     */
    public void setServerName(String localHostname) {
        this.serverName = localHostname;
    }

    /**
     * Get the local hostname of the installation server.
     * @return the hostname
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Set the location of the tree
     * @param url to set.
     */
    public void setBasePath(String url) {
        this.tree.setBasePath(url);
    }

    /**
     * Set the Channel for this tree
     * @param channelIn to set
     */
    public void setChannel(Channel channelIn) {
        this.tree.setChannel(channelIn);
    }

    /**
     * Replace legacy package names with empty string for each PackageListItem
     * in the provided list.
     * @param packageListItems List of PackageListItems to be modified in place.
     */
    private void replaceLegacyPackageNames(List packageListItems) {
        // munge the list of auto kickstarts
        for (Object packageListItemIn : packageListItems) {
            PackageListItem pli = (PackageListItem) packageListItemIn;
            pli.setName(pli.getName().replaceFirst(
                    KickstartData.LEGACY_KICKSTART_PACKAGE_NAME, EMPTY_STRING));
        }
    }

    /**
     * Return all channels available for creating kickstartable trees.
     * @return list of channels
     */
    public List<Channel> getKickstartableTreeChannels() {
        return ChannelFactory.getKickstartableTreeChannels(user.getOrg());
    }

    /**
     * Get the CobblerCommand class associated with this operation.
     * Determines which Command we should execute when calling store()
     *
     * @return CobblerCommand instance.
     */
    protected abstract CobblerCommand getCobblerCommand();


    /**
     * @return Returns the kernelOptionsPost.
     */
    public String getKernelOptionsPost() {
        return tree.getKernelOptionsPost();
    }


    /**
     * @param kernelOptionsPostIn The kernelOptionsPost to set.
     */
    public void setKernelOptionsPost(String kernelOptionsPostIn) {
        tree.setKernelOptionsPost(kernelOptionsPostIn);
    }


    /**
     * @return Returns the kernelOptions.
     */
    public String getKernelOptions() {
        return tree.getKernelOptions();
    }


    /**
     * @param kernelOptionsIn The kernelOptions to set.
     */
    public void setKernelOptions(String kernelOptionsIn) {
        tree.setKernelOptions(kernelOptionsIn);
    }

}
