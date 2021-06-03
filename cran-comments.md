## Test environments

* macOS 11.1 (64-bit) locally, R 3.6.2
* Ubuntu 20.04.1 (64-bit) on Rhub, R 4.1.0
* Debian (64-bit) on Rhub, R-devel

## R CMD check results

There were no ERRORs or WARNINGs.

There was 1 NOTE about the fact that this package was updated on CRAN 0 days ago:

checking CRAN incoming feasibility ... NOTE
Maintainer: ‘Christopher Baker <chriscrewbaker@gmail.com>’
Days since last update: 0

This NOTE is happening because rapidraker was recently accepted on CRAN, but a subsequent build on CRAN servers resulted in an error (https://cran.r-project.org/web/checks/check_results_rapidraker.html). This error was probably occurring b/c I had compiled my jar under Java 8, which may have been imposing a minimum Java version that was not available on r-patched-solaris-x86. As per "Writing R Extensions," I have recompiled to target Java 6.

I was able to reproduce the error by trying to run my jar on Java 6, which threw the same error that I was getting on r-patched-solaris-x86. After recompiling for Java 6, I no longer get that error locally.

## Downstream dependencies

There are no downstream dependencies
