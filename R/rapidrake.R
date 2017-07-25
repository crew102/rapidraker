#' Rapid RAKE
#'
#' A relatively fast version of the Rapid Automatic Keyword Extraction (RAKE) algorithm. See \href{Automatic keyword extraction from individual documents}{http://media.wiley.com/product_data/excerpt/22/04707498/0470749822.pdf} for details on how RAKE works.
#'
#' @param txt
#' @param stop_words
#' @param word_min_char
#' @param stem
#' @param keep_pos
#'
#' @return
#' @export
#'
#' @examples
rapidrake <- function(txt, stop_words = slowraker::smart_words,
                      word_min_char = 3, stem = TRUE,
                      keep_pos = c("NN", "NNS", "NNP", "NNPS")) {

}