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
  key_cnts <- table(keyword_df$keyword)
  key_cntsdf <- as.data.frame(key_cnts, stringsAsFactors = FALSE)
  colnames(key_cntsdf) <- c("keyword", "freq")
  key_df <- merge(key_cntsdf, keyword_df, by = "keyword")
  out_df <- unique(key_df[order(key_df$score, decreasing = TRUE), ])
  row.names(out_df) <- NULL
  out_df
}