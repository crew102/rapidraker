.onLoad <- function(libname, pkgname) {
  rJava::.jpackage(pkgname, lib.loc = libname)
  ## drop this after dev:
  # rJava::.jaddClassPath(dir(file.path(getwd(), "inst/java"), full.names = TRUE))
}