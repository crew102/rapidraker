#' Rapid RAKE
#'
#' A relatively fast version of the Rapid Automatic Keyword Extraction (RAKE)
#' algorithm. See \href{http://media.wiley.com/product_data/excerpt/22/04707498/0470749822.pdf}{Automatic keyword extraction from individual documents} for details on
#' how RAKE works.
#'
#' @inheritParams slowraker::slowrake
#' @export
#'
#' @examples
rapidrake <- function(txt, stop_words = slowraker::smart_words,
                      stop_pos = c("VB", "VBD", "VBG", "VBN", "VBP", "VBZ"),
                      word_min_char = 3, stem = TRUE) {

  tagger_bin <- system.file("models/en-pos-maxent.bin", package = "openNLPdata")

  rake_params <- rJava::new(
    rJava::J("org.crew102.rapidrake.model.RakeParams"),
    stop_words, stop_pos, as.integer(word_min_char), stem
  )

  java_array_refs <- rJava::.jarray(txt)

  rake_alg <- rJava::new(
    rJava::J("org.crew102.rapidrake.RakeAlgorithm"),
    rake_params, tagger_bin
  )

  for (i in seq_along(java_array_refs)) {

    result <- rake_alg$rake(java_array_refs[[i]])

    result$getFullKeywords()

  }
}