FROM rocker/tidyverse

ADD . /home/rapidraker

WORKDIR /home/rapidraker

RUN apt-get update; \
  apt-get install -y --no-install-recommends openjdk-8-jdk maven r-base-dev; \
  R CMD javareconf; \
  git clone https://github.com/crew102/rapidrake-java.git ../rapidrake-java; \
  Rscript -e "devtools::install_deps(dependencies = TRUE, quiet = TRUE)"; \
  make build-jar