# Base image for all the following docker builds

The purpose of this image is to enable environment-specific modifications like corporate proxies or custom root
CA certificates.

If you are using K3S Docker Registry, make sure to push the final image there, because Maven Docker plugin will try
to download the latest version.