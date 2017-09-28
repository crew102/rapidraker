#' Rapid RAKE
#'
#' A relatively fast version of the Rapid Automatic Keyword Extraction (RAKE) algorithm. See \href{http://media.wiley.com/product_data/excerpt/22/04707498/0470749822.pdf}{Automatic keyword extraction from individual documents} for details on how RAKE works.
#'
#' @inheritParams slowraker::slowrake
#' @export
#'
#' @examples
rapidrake <- function(txt, stop_words = slowraker::smart_words,
                      filter_pos = c("VB", "VBD", "VBG", "VBN", "VBP", "VBZ"),
                      word_min_char = 3, stem = TRUE) {
  rkdr <- rJava::.jnew("org.crew102.rapidrake.RakeWrapper", txt,
                      stop_words, filter_pos, word_min_char, stem)
  rJava::.jcall(rkdr, "[<String>", "run")
}