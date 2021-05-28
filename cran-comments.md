## Test environments

* macOS 11.1 (64-bit) locally, R 3.6.2
* Ubuntu 20.04.2 (64-bit) on a Docker-based build on Travis-CI, R 4.0.5
* Debian (64-bit) on Rhub, R-devel

## R CMD check results

There were no ERRORs or WARNINGs.

There was 1 NOTE about this package having been archived on CRAN, stating:

Package was archived on CRAN

CRAN repository db overrides:
  X-CRAN-Comment: Archived on 2019-12-19 as check problems were not
    corrected in time.

  States it require Java >= 7 but actually requires >= 8

## Downstream dependencies

There are no downstream dependencies

## First resubmission

This is a resubmission. In this version I have:

* Fixed one of the NOTEs that was occurring because there were possibly misspelled words in DESCRIPTION. These words were author names, and I have since enclosed them in single parentheses.

* Fixed one of the NOTEs that was occurring because there was an example with CPU time > 2.5 times elapsed time. I have since wrapped my example in a dontrun command.

* I still receive a NOTE regarding the package being archived on CRAN, but I believe that can safely be ignored.

## Second resubmission

This my second resubmission. In this version I have documented the output of the rapidrake function. Also, regarding the reason why this package was archived, I had previously specified a dependency on Java >= 7, which was resulting in failed builds on CRAN b/c the package was needing Java >= 8 to properly load. I have since updated the Java requirement to be >= 8.
