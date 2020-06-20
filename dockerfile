FROM rocker/tidyverse

ADD . /home/rapidraker

WORKDIR /home/rapidraker

# Install all of the deps that are required to build and test both rapidrake-java
# and rapidraker

RUN apt-get update; \
  apt-get install -y --no-install-recommends openjdk-8-jdk maven r-base-dev; \
  update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java; \
  R CMD javareconf; \
  cd ..; \
  git clone https://github.com/crew102/rapidrake-java.git; \
  mkdir rapidrake-java/model-bin; \
  wget -P rapidrake-java/model-bin http://opennlp.sourceforge.net/models-1.5/en-sent.bin; \
  wget -P rapidrake-java/model-bin http://opennlp.sourceforge.net/models-1.5/en-pos-maxent.bin; \
  cd rapidraker; \
  Rscript -e "devtools::install_deps(dependencies = TRUE)";
