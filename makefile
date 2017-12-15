all: build-jar doc test README.md install-package

# Build java jars and move into inst/java
build-jar: $(/usr/bin/find java -type f -iname "*\.java") java/rapidrake/pom.xml
	$(MAKE) -C java/rapidrake
	rm -rf inst/java
	mkdir -p inst/java
	cp java/rapidrake/target/*.jar inst/java

# Install package locally
install-package:
	cd ..; R CMD INSTALL rapidraker --no-multiarch

# Render README.Rmd to README.md
README.md: README.Rmd
	Rscript -e "rmarkdown::render('README.Rmd', output_file = 'README.md', output_dir = getwd(), output_format = 'github_document', quiet = TRUE)"

# Document package
doc:
	Rscript -e "devtools::document(); source('inst/make-helpers.R')"

# Test package
check:
		Rscript -e "devtools::check()"

# Clean
clean:
	rm README.md
	rm -rf inst/java

# Build binary
build:
	cd ..; R CMD INSTALL rapidraker --build --no-multiarch