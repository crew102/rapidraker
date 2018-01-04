all: build-jar doc README.md test

# Build java jars and move into inst/java (also runs tests on Java side)
build-jar:
	$(MAKE) -C ../rapidrake-java
	rm -rf inst/java
	mkdir -p inst/java
	cp $(shell ls ../rapidrake-java/target/*\.jar | grep -vE "javadoc|sources|opennlp") inst/java

# Render README.Rmd to README.md
README.md: README.Rmd
	Rscript -e "rmarkdown::render('README.Rmd', output_file = 'README.md', output_dir = getwd(), output_format = 'github_document', quiet = TRUE)"
	rm README.html

# Document package
doc:
	Rscript -e "devtools::document(); source('inst/make-doc-helpers.R')"

# Run R tests
test:
	cd ..; R CMD build rapidraker && R CMD check $(shell ls | grep "tar\\.gz") --as-cran --no-manual

# Clean
clean:
	rm README.md
	rm -rf inst/java