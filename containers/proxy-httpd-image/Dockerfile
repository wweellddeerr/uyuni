#!BuildTag: uyuni/proxy-httpd:latest uyuni/proxy-httpd:%PKG_VERSION% uyuni/proxy-httpd:%PKG_VERSION%.%RELEASE%

ARG BASE=registry.suse.com/bci/bci-base:15.3
FROM $BASE


ARG PRODUCT_REPO
ARG PRODUCT=Uyuni
ARG VENDOR="Uyuni project"
ARG URL="https://www.uyuni-project.org/"
ARG REFERENCE_PREFIX="registry.opensuse.org/uyuni"

# Add distro and product repos
COPY add_repos.sh /usr/bin
RUN sh add_repos.sh ${PRODUCT_REPO}

# Build Service required labels
# labelprefix=org.opensuse.uyuni.proxy-httpd
LABEL org.opencontainers.image.title="${PRODUCT} proxy httpd container"
LABEL org.opencontainers.image.description="Image contains a ${PRODUCT} proxy component to serve http requests"
LABEL org.opencontainers.image.created="%BUILDTIME%"
LABEL org.opencontainers.image.vendor="${VENDOR}"
LABEL org.opencontainers.image.url="${URL}"
LABEL org.opencontainers.image.version="%PKG_VERSION%"
LABEL org.openbuildservice.disturl="%DISTURL%"
LABEL org.opensuse.reference="${REFERENCE_PREFIX}/proxy-httpd:%PKG_VERSION%.%RELEASE%"
# endlabelprefix

# http(s)
EXPOSE 80/tcp
EXPOSE 443/tcp

VOLUME ["/etc/uyuni", "/srv/tftpboot", "/var/cache/rhn"]

RUN zypper --gpg-auto-import-keys --non-interactive install --auto-agree-with-licenses \
 python3-rhnlib spacewalk-proxy-broker \
 spacewalk-proxy-common spacewalk-proxy-package-manager \
 spacewalk-proxy-redirect spacewalk-ssl-cert-check spacewalk-proxy-html \
 susemanager-tftpsync-recv

RUN zypper --gpg-auto-import-keys --non-interactive install --auto-agree-with-licenses python3-PyYAML

RUN sysconf_addword /etc/sysconfig/apache2 APACHE_MODULES headers
RUN sysconf_addword /etc/sysconfig/apache2 APACHE_MODULES proxy_http

COPY uyuni-configure.py /usr/bin/uyuni-configure.py
RUN chmod +x /usr/bin/uyuni-configure.py

CMD uyuni-configure.py && /usr/sbin/start_apache2 -DFOREGROUND -k start
