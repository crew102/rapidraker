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

  pos_tagger <- rJava::.jnew(
    "org.crew102.rapidrake.model.Tagger",
    tagger_bin
  )

  rake_alg <- rJava::.jnew(
    "org.crew102.rapidrake.RakeAlgorithm",
    stop_words, stop_pos, as.integer(word_min_char), stem,
    pos_tagger
  )
  doc <- rJava::.jnew(
    "org.crew102.rapidrake.model.Document",
    txt[1]
  )
  rJava::.jcall(obj = rake_alg, returnSig = "[S", method = "rake", doc, evalString = F)
}