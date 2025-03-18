FROM ubuntu:20.04
LABEL maintainer="Patrick.Reignier@imag.fr"

# Set timezone and install packages
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -yq tzdata && \
    ln -fs /usr/share/zoneinfo/Europe/Paris /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata

RUN apt-get install -yq diffutils wget sudo openjdk-16-jdk-headless make gcc gnat-9 locales git time maven

# Configure locale
RUN locale-gen --no-purge en_US.UTF-8
ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en

# Setup ima_sources directories and files
RUN mkdir -p /usr/local/ima
COPY ima_sources/bin /usr/local/ima/bin
COPY ima_sources/sources /usr/local/ima/sources

# Add user 'gl' with sudo rights
RUN useradd -ms /bin/bash gl && usermod -aG sudo gl
RUN echo gl:gl | chpasswd

# Copy bashrc and set permissions (as root)
COPY docker/bashrc /home/gl/.bashrc
RUN chown gl:gl /home/gl/.bashrc

# Ensure directories exist and append paths (as root)
RUN mkdir -p /home/gl/Projet_GL /home/gl/.config \
    && chown -R gl:gl /home/gl \
    && echo 'export PATH="$PATH:/home/gl/Projet_GL/src/main/bin/"' >> /home/gl/.bashrc \
    && echo 'export PATH="$PATH:/home/gl/Projet_GL/src/test/script/launchers"' >> /home/gl/.bashrc

# Now switch to user gl
USER gl

# Set volume and workdir
VOLUME /home/gl/Projet_GL
WORKDIR /home/gl/Projet_GL

# Start bash shell to keep the container running
ENTRYPOINT ["/bin/bash"]

