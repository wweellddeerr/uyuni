/*
 * Copyright (c) 2022 SUSE LLC
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
package com.redhat.rhn.frontend.servlets;

import com.redhat.rhn.frontend.taglibs.DWRItemSelector;

import com.google.gson.Gson;

import java.io.BufferedReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DwrSumaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            DWRItemSelectorReq item = parseBody(req);
            String response = new DWRItemSelector().select(req, item.set_label, item.values, item.checked);
            resp.getOutputStream().print(response);
            resp.getOutputStream().close();
        }
        catch (Exception eIn) {
            throw new ServletException(eIn);
        }
    }

    private static class DWRItemSelectorReq {
        private String set_label;
        private String[] values;
        private boolean checked;
    }
    private DWRItemSelectorReq parseBody(HttpServletRequest req) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return new Gson().fromJson(sb.toString(), DWRItemSelectorReq.class);

    }
}
