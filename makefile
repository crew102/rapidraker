all: README.md doc test

# Render README.Rmd to README.md
README.md: README.Rmd
	Rscript -e "rmarkdown::render('README.Rmd', output_file = 'README.md', output_dir = getwd(), output_format = 'github_document', quiet = TRUE)"
	rm README.html

# Document package
doc:
	Rscript -e "devtools::document()"

# Test package
test:
	Rscript -e "devtools::test()"

# Clean
clean:
	rm README.md