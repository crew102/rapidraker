# Build Java jars if needed and move them into inst/java (also runs
# rapidraker-java unit tests along the way).
build-jar:
	$(MAKE) -C ../rapidrake-java # note that rapidrake-java was pulled from github in dockerfile
	rm -rf inst/java
	mkdir -p inst/java
	cp ../rapidrake-java/target/opennlp*[0-9]\.jar inst/java
	cp ../rapidrake-java/target/rapidrake*[0-9]\.jar inst/java

build-package:
	cd ..; R CMD build rapidraker

install-package:
	cd ..; R CMD INSTALL rapidraker*tar\.gz

test:
	cd ..; R CMD check rapidraker*tar\.gz --as-cran

README.md: README.Rmd
	Rscript -e "rmarkdown::render('README.Rmd', output_file = 'README.md', output_dir = getwd(), output_format = 'github_document', quiet = TRUE)"
	rm README.html

doc:
	Rscript -e "devtools::document(); source('inst/make-doc-helpers.R')"

clean:
	- rm -rf inst/java README.md ../rapidraker*tar\.gz rapidraker*tar\.gz

cran: clean README.md doc build-jar build-package install-package test
	mv ../rapidraker*tar\.gz .

ci-build: build-jar build-package install-package test
