.onLoad <- function(libname, pkgname) {
  rJava::.jpackage(pkgname, lib.loc = libname)
  # rJava::.jaddClassPath(list.files('inst/java', full.names = TRUE))
}