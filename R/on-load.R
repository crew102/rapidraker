.onLoad <- function(libname, pkgname) {
  rJava::.jpackage(pkgname, lib.loc = libname)
  ### need to drop this line for cran:
  rJava::.jaddClassPath(list.files('inst/java', full.names = TRUE))
}