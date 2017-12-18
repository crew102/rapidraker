# Having rJava::.jaddClassPath(list.files('inst/java', full.names = TRUE))
# inside .onLoad() allows testthat to find classes when testing. However,
# we need to remove this line whenever pushing to cran.

mnfile <- file("R/on-load.R")
lines <- readLines(mnfile)
lines[4] <- paste0("#", lines[4])
writeLines(lines, mnfile)
close(mnfile)