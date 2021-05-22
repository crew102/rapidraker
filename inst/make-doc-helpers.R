# inheritParams has trouble getting this roxygen comment correct, so have to
# manually override .Rd file

mnfile <- file("man/rapidrake.Rd")
rrrd <- readLines(mnfile)
new_rrrd <- gsub(
  "\\Q\\href{{https://rdrr.io/rforge/tm/man/stopwords.html}{tm::stopwords('SMART')}}\\E",
  "\\\\href{https://rdrr.io/rforge/tm/man/stopwords.html}{tm::stopwords('SMART')}",
  rrrd
)
writeLines(new_rrrd, mnfile)
close(mnfile)
