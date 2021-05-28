#' Rapid RAKE
#'
#' A relatively fast version of the Rapid Automatic Keyword Extraction (RAKE)
#' algorithm. See \href{http://media.wiley.com/product_data/excerpt/22/04707498/0470749822.pdf}{
#' Automatic keyword extraction from individual documents} for details on how
#' RAKE works.
#'
#' @inheritParams slowraker::slowrake
#' @param phrase_delims A regular expression containing the characters that
#' will be used as phrase delimiters
#'
#' @inherit slowraker::slowrake return
#'
#' @export
#'
#' @importFrom slowraker smart_words
#' @import openNLPdata
#'
#' @examples
#' \dontrun{
#' rakelist <- rapidrake(txt = "some text that has great keywords")
#' slowraker::rbind_rakelist(rakelist)
#' }
rapidrake <- function(txt,
                      stop_words = slowraker::smart_words,
                      stop_pos = c("VB", "VBD", "VBG", "VBN", "VBP", "VBZ"),
                      word_min_char = 3,
                      stem = TRUE,
                      phrase_delims = "[-,.?():;\"!/]") {

  rake_params <- rJava::new(
    rJava::J("io.github.crew102.rapidrake.model.RakeParams"),
    rJava::.jcastToArray(empty_if_null(stop_words)),
    rJava::.jcastToArray(empty_if_null(stop_pos)),
    as.integer(word_min_char),
    stem,
    phrase_delims
  )

  java_array_refs <- rJava::.jarray(txt)

  rake_alg <- rJava::new(
    rJava::J("io.github.crew102.rapidrake.RakeAlgorithm"),
    rake_params,
    system.file("models/en-pos-maxent.bin", package = "openNLPdata"),
    system.file("models/en-sent.bin", package = "openNLPdata")
  )

  num_docs <- length(txt)
  multi_docs <- num_docs != 1

  if (multi_docs) {
    prog_bar <- utils::txtProgressBar(min = 0, max = num_docs, style = 3)
  }

  all_out <- vector(mode = "list", length = num_docs)

  for (i in seq_along(java_array_refs)) {

    result <- rake_alg$rake(java_array_refs[[i]])

    all_out[[i]] <- process_keyword_df(
      data.frame(
        keyword = result$getFullKeywords(),
        score = result$getScores(),
        stem = result$getStemmedKeywords(),
        stringsAsFactors = FALSE
      )
    )

    if (multi_docs) {
      utils::setTxtProgressBar(prog_bar, i)
    }
  }

  structure(all_out, class = c(class(all_out), "rakelist"))
}

process_keyword_df <- function(keyword_df) {
  if (nrow(keyword_df) == 0) return(NA)
  key_cnts <- table(keyword_df$keyword)
  key_cntsdf <- as.data.frame(key_cnts, stringsAsFactors = FALSE)
  colnames(key_cntsdf) <- c("keyword", "freq")
  key_df <- merge(key_cntsdf, keyword_df, by = "keyword")
  out_df <- unique(key_df[order(key_df$score, decreasing = TRUE), ])
  row.names(out_df) <- NULL
  out_df
}

empty_if_null <- function(x) if (is.null(x)) "" else x
