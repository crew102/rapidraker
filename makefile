all: build-jar doc README.md install-package test

# Build java jars and move into inst/java (also runs tests on Java side)
build-jar:
	$(MAKE) -C ../rapidrake-java
	rm -rf inst/java
	mkdir -p inst/java
	cp ../rapidrake-java/target/*\.jar inst/java

# Install package locally
install-package:
	cd ..; R CMD INSTALL rapidraker --build --no-multiarch
	cd ..; R CMD INSTALL rapidraker*\.zip --no-multiarch

# Render README.Rmd to README.md
README.md: README.Rmd
	Rscript -e "rmarkdown::render('README.Rmd', output_file = 'README.md', output_dir = getwd(), output_format = 'github_document', quiet = TRUE)"
	rm README.html

# Document package
doc:
	Rscript -e "devtools::document(); source('inst/make-doc-helpers.R')"

# Run R tests
test:
	Rscript -e "devtools::test()"

# Clean
clean:
	rm README.md
	rm -rf inst/java

cran:
	Rscript -e "source('inst/make-classpath-helpers.R')"