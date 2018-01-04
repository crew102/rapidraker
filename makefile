all: build-jar README.md doc test

# All jobs are meant to be run inside docker container defined in dockerfile

# Build java jars and move into inst/java (also runs tests on Java side)
build-jar:
	$(MAKE) -C ../rapidrake-java # note that rapidrake-java was pulled from github in dockerfile
	rm -rf inst/java
	mkdir -p inst/java
	cp ../rapidrake-java/target/*[0-9]\.jar inst/java # copy rapidrake and opennlp jars

# Render README.Rmd to README.md
README.md: build-package README.Rmd
	R CMD INSTALL ../rapidraker*tar\.gz
	Rscript -e "rmarkdown::render('README.Rmd', output_file = 'README.md', output_dir = getwd(), output_format = 'github_document', quiet = TRUE)"
	rm README.html

# Document package
doc:
	Rscript -e "devtools::document(); source('inst/make-doc-helpers.R')"

build-package:
	cd ..; R CMD build rapidraker

# Run R tests
test: build-package
	cd ..; R CMD check rapidraker*tar\.gz --as-cran --no-manual

# Clean
clean:
	rm README.md
	rm -rf inst/java

# Run all jobs required to create most recent version of package (build-jar,
# readme, doc), build and test package, then move tarball inside rapidraker dir
# so that it is mapped to the docker host
cran: all
	mv ../rapidraker*tar\.gz .