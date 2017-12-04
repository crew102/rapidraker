rapidraker
================

> A fast version of the Rapid Automatic Keyword Extraction (RAKE) algorithm

[![Linux Build Status](https://travis-ci.org/crew102/rapidraker.svg?branch=master)](https://travis-ci.org/crew102/rapidraker) [![AppVeyor Build Status](https://ci.appveyor.com/api/projects/status/o8nxlmteivknib4d?svg=true)](https://ci.appveyor.com/project/crew102/rapidraker)

Installation
------------

You can get the stable version from CRAN:

``` r
install.packages("rapidraker")
```

Or the development version from GitHub:

``` r
if (!require(devtools)) install.packages("devtools")

devtools::install_github("crew102/rapidraker")
```

What is `rapidraker`?
---------------------

`rapidraker` is an R package that provides an implementation of the same keyword extraction algorihtm (RAKE) that the `slowraker` package does. However, `rapidraker::rapidrake()` is written mostly in Java, whereas `slowraker::slowrake()` is written mostly in R. This means that you can expect `rapidrake()` to be considerably faster than `slowrake()`.

Usage
-----

`rapidrake()` has the same arguments as `slowrake()`, and both functions output the same type of object. You can therefore substitue `rapidrake()` for `slowraker()` without making any additional changes to your code.

``` r
library(slowraker)
library(rapidraker)

data("dog_pubs")
rakelist <- rapidrake(txt = dog_pubs$abstract[1:5])
```

`rapidrake()` outputs a list of data frames. Each data frame contains the keywords that were extracted for an element of `txt`:

``` r
rakelist
#> 
#> # A rakelist containing 5 data frames:
#>  $ :'data.frame':    61 obs. of  4 variables:
#>   ..$ keyword:"assistance dog identification tags" ...
#>   ..$ freq   :1 1 ...
#>   ..$ score  :11 ...
#>   ..$ stem   :"assist dog identif tag" ...
#>  $ :'data.frame':    90 obs. of  4 variables:
#>   ..$ keyword:"current dog suitability assessments focus" ...
#>   ..$ freq   :1 1 ...
#>   ..$ score  :22 ...
#>   ..$ stem   :"current dog suitabl assess focus" ...
#> #...With 3 more data frames.
```

You can bind these data frames together using `slowaker::rbind_rakelist()`:

``` r
rakedf <- rbind_rakelist(rakelist = rakelist, doc_id = dog_pubs$doi[1:5])
head(rakedf, 5)
#>                         doc_id                            keyword freq score                   stem
#> 1 10.1371/journal.pone.0132820 assistance dog identification tags    1    11 assist dog identif tag
#> 2 10.1371/journal.pone.0132820          animal control facilities    1     9     anim control facil
#> 3 10.1371/journal.pone.0132820          emotional support animals    1     9      emot support anim
#> 4 10.1371/journal.pone.0132820                   small body sizes    1     9        small bodi size
#> 5 10.1371/journal.pone.0132820       seemingly inappropriate dogs    1     8    seem inappropri dog
```

Learning more
-------------

-   To learn more about the API used by both `slowraker` and `rapidraker`, head over `slowraker`'s [webpage](https://crew102.github.io/slowraker/index.html).
