# ~/.bashrc: executed by bash(1) for non-login shells.
# See /usr/share/doc/bash/bash.pdf.

# Current prompt, LPImmes
# Page 69-70 Learning Bash shell
# \W  means current dir. only, not path
# \! means history count- not incremental, since I delete things from history
# See below for ignore.
# Use the history number as prompt itself, followed by a space.
#PS1='\W \!>'

# http://volnitsky.com/project/git-prompt/
# http://coderjournal.com/2011/04/gitconfig/
# https://github.com/git/git/blob/master/contrib/completion/git-prompt.sh
[[ $- == *i* ]]   &&   . /Users/lpimmes/git-prompt/git-prompt.sh

# From linux, but bogus now.
SCRIPTS="$HOME/.gnome2/nautilus-scripts"

# Can echo, but not ls $FLEX_HOME
#FLEX_HOME=/Volumes/"Macintosh HD"/Applications/"Adobe Flex Builder 3"/sdks/3.2.0
# copy directory original directory, so that:
#~ 504>echo $FLEX_HOME
#/Users/lpimmes/Documents/3.2.0
#~ 504>ls $FLEX_HOME
# $ grails flex-init ;; now works
#FLEX_HOME=~/Documents/3.2.0
# place path in Config.groovy
# grails.plugin.flex.home = '/Users/lpimmes/Downloads/flex_sdk_4'
FLEX_HOME=/Users/lpimmes/Downloads/flex_sdk_4

#~ 494>echo $PATH | tr ':' '\n'

# ../Home has a bin directory, makes ant happy
JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
J2EE_HOME=/Applications/NetBeans/glassfish-3.1.1

# Updated frequently!
ROO_HOME=/Users/lpimmes/springsource/spring-roo-1.2.2.RELEASE 

#p38 java ee 6 tutorial; AS means application server
AS_INSTALL_PARENT=/Users/lpimmes/glassfish3
AS_INSTALL=/Users/lpimmes/glassfish3/glassfish
DERBY_HOME=$AS_INSTALL_PARENT/javadb
#p41, ditto
#ANT=$AS_INSTALL_PARENT/ant 
ANT=/Users/lpimmes/Desktop/apache-ant-1.8.4
JMETER_HOME=/Users/lpimmes/Downloads/apache-jmeter-2.6
MORPH=/Users/lpimmes/Desktop/netarose/thirdparty/JavaLibraries/morphadorner-2009-04-30/lib

# Updated groovy, grails, griffon 10/14/2012
GRIFFON_HOME=/Users/lpimmes/Downloads/griffon-1.1.0

# GROOVY_HOME=~/Downloads/groovy-1.7.3/bin
GROOVY_HOME=/Users/lpimmes/Downloads/groovy-2.0.5

# GRAILS_HOME=~/Downloads/grails-1.3.7/bin
# GRAILS_HOME=/Users/lpimmes/Downloads/grails-1.4.0.M1
GRAILS_HOME=/Users/lpimmes/Downloads/grails-2.1.1


SUBVERSION=/opt/subversion/bin
MACPRTS=/opt/local/bin
MERCURYPROGRAMS=/Users/lpimmes/Documents/lpimmesOnly/mercuryPrograms
# was 11.01
# comment this when building next version
# You need a working mercury compiler to bootstrap with! So don't comment out
#MERCURY=/usr/local/mercury-11.07.1/bin
MERCURY=/usr/local/mercury-11.07.2/bin



# 11/07/2011
#Don't use this gcc on Lion, after xcode installed.
#mercury-compiler-11.01 138>which gcc
#/usr/bin/gcc
# Use gcc from /usr/local/bin
# which came from ~/Downloads/gcc-lion.function tar () {
#	bin 95>ls
#	cpp		g++		gcc		gcov		gfortran
#	bin 95>pwd
#	/Users/lpimmes/Downloads/usr/local/bin

# If you untar this file, it will go into /usr/local/bin

# ~ 236>which gcc
# /Users/lpimmes/Downloads/usr/local/bin/gcc
# ~ 236>gcc -v
# Using built-in specs.
# COLLECT_GCC=gcc
# COLLECT_LTO_WRAPPER=/Users/lpimmes/Downloads/usr/local/bin/../libexec/gcc/x86_64-apple-darwin11.0.0/4.6.1/lto-wrapper
# Target: x86_64-apple-darwin11.0.0
# Configured with: ../gcc-4.6.1/configure --enable-languages=fortran,c++
# Thread model: posix
# gcc version 4.6.1 (GCC) 

# Not working either: use Mercury site to find gcc, and use one of the mirror sites:
# Building gcc from sratch; not what I want.
# Use snow leapord disk, optional software, xcode and install via cdrom.

# This works! No compiler errors when invoking 'make'.
# ~ 258>which gcc
# /usr/bin/gcc
# ~ 257>gcc -v
# Using built-in specs.
# Target: i686-apple-darwin10
# Configured with: /var/tmp/gcc/gcc-5646~6/src/configure --disable-checking --enable-werror --prefix=/usr --mandir=/share/man --enable-languages=c,objc,c++,obj-c++ --program-transform-name=/^[cg][^.-]*$/s/$/-4.2/ --with-slibdir=/usr/lib --build=i686-apple-darwin10 --with-gxx-include-dir=/include/c++/4.2.1 --program-prefix=i686-apple-darwin10- --host=x86_64-apple-darwin10 --target=i686-apple-darwin10
# Thread model: posix
# gcc version 4.2.1 (Apple Inc. build 5646)

#/Users/lpimmes/Downloads/usr/local/bin:\

#http://www.janhellevik.no/blog/?cat=3
# ORACLE client
ORACLE_INSTANTCLIENT=~/Downloads/instantClient_10.2
DYLD_LIBRARY_PATH=$ORACLE_INSTANTCLIENT
TNS_ADMIN=$ORACLE_INSTANTCLIENT/network/admin
ORACLE_SID=AL32UTF8
# ORACLE client

# Don't use apache nor mysql for now.
# Apples ships with apache and php; working now.
# Mysql does not appear to support UTF8. But that could be the browser,
#although I doubt it.
# from php
#MYSQL=/Applications/XAMPP/xamppfiles/bin

# For weave 91.641
MYSQL=/Usr/local/mysql/bin
APACHE=/usr/local/apache-tomcat-7.0.25/bin
SCALA_HOME=/Users/lpimmes/Downloads/scala-2.10.0
MD=/Users/lpimmes/Documents/lpimmesOnly/important_misc
H2=/Users/lpimmes/Downloads/h2
PROLOGTREE=/Users/lpimmes/Documents/lpimmesOnly/UMassLowell/91.701_Levkowitz_summer2012/prolog/lojban/
GIT=/usr/local/git 
USSR=/Users/lpimmes/Documents/lpimmesOnly/russianLetters/recent_ussr-star.com

#binary
MONGODB=/Users/lpimmes/Documents/lpimmesOnly/mongodb-osx-x86_64-2.2.0
PYALGOR=/Users/lpimmes/Documents/lpimmesOnly/UMassLowell/91.503_analysisAlgor/algor_ebooks/python-algor 


#take out for now, since I want normal mac's ruby
#$HOME/.rvm/bin:\
#$GEMBIN:\

#~ 484>which ruby
#/usr/bin/ruby
#~ 480>which rails
#/usr/bin/rails
#~ 480>ruby --version
#ruby 1.8.7 (2010-01-10 patchlevel 249) [universal-darwin11.0]
#~ 480>rails --version
#Rails 3.2.2
#~ 480>


#no need for JAVA on PATH
# this is where gcc (new) for mac is, because I had a ruby compile problem:

#mercury-compiler-11.07.1 488>which gcc
#/Developer/usr/bin:
# But now I cannot compile mercury, non-llvm version of gcc-4.2
# 02/19/2013
#   https://github.com/wayneeseguin/rvm/issues/1351#issuecomment-10939525
# Do not bother with rvm, will not compile ruby from source.
# readline errors.
DART_HOME=/Users/lpimmes/Downloads/dart/dart-sdk

CLOJURE_HOME=/Users/lpimmes/Downloads/clojure-1.5.1

# Unlike PATH, you must include specific *.jar
CLASSPATH=.:$CLOJURE_HOME/clojure-1.5.1.jar

# Last login: Fri Mar 29 23:11:08 on ttys007
# luke-immess-imac-2:~ lpimmes$ bash
# ~ 501>java -classpath $CLASSPATH clojure.main
# Clojure 1.5.1
# user=> 
# user=> ^C~ 500>

# ~ 499>java clojure.main
# Clojure 1.5.1
# user=>

LEIN_HOME=/Users/lpimmes/leiningen

PATH=$MERCURY:\
$LEIN_HOME/bin:\
$DART_HOME/bin:\
$MONGODB/bin:\
$GIT/bin:\
$GROOVY_HOME/bin:\
$GRAILS_HOME/bin:\
$GRIFFON_HOME/bin:\
$PROLOGTREE:\
/Developer/usr/bin:\
/usr/local/bin:\
$MD:\
$H2/bin:\
$SCALA_HOME/bin:\
$JMETER_HOME/bin:\
$JAVA_HOME/bin:\
$AS_INSTALL_PARENT/bin:\
$AS_INSTALL/bin:\
$ANT/bin:\
$DERBY_HOME/bin:\
$MYSQL:\
$APACHE:\
$MERCURYPROGRAMS:\
$ROO_HOME/bin:\
$ORACLE_INSTANTCLIENT:\
$MACPRTS:\
$SUBVERSION:\
/usr/texbin:\
${PATH}:\
/usr/lib:

alias ep='alias; echo ""; echo $PATH | tr ":" "\n"'
# was 11.01
MANPATH=/usr/local/mercury-11.07.1/man:\
${MANPATH}:/usr/local/pgsql/man
   

# was 11.01
INFOPATH=/usr/local/mercury-11.07.1/info:\
${INFOPATH}

trdCodePath=$HOME/Documents/trdCode
dbsqlPath=$HOME/Documents/trdCode
stocksCvsPath=$HOME/Documents/trdRsrch/histPrices/yahooOrig
stocksFilterCvsPath=$dbsqlPath

# for root's editing of sudoers file
# su (use root password)
# visudo  (emacs will be used as editor)
# See Linux Cookbook for discussion
# Editing file is file, but lpimmes does not have su privelages.
# I am not sure why not.
#
VISUAL=emacs

#History options, p64, Learning the BASH Shell
HISTTIMEFORMAT="%a %x %X %Z  " # current locale's dayOfWeek, date, time, tzone format
#HISTIGNORE="popd*:cd*:history*:&" 
# ignore common commands; & means ignore duplicates
# Don't ignore pushd * because I usually will want to re-evaluate this command
# and its argument.

HISTCONTROL="ignoreboth:erasedups" 
SZ=5000  # I want to review what I did in distant past.
HISTFILESIZE=$SZ #max num. lines in file
HISTSIZE=$SZ #max hist. lines to remember

# On mac lion sql client (32 bit), try:
# SQL> select * from v$nls_parameters where parameter like '%CHARACTERSET%';
# 
# PARAMETER
# ----------------------------------------------------------------
# VALUE
# ----------------------------------------------------------------
# NLS_CHARACTERSET  ;; varchar2
# AL32UTF8
# 
# NLS_NCHAR_CHARACTERSET ;; nvarchar2
# AL16UTF16 

#SQL> @cs.sql

#NAME	      VALUE
#------------- ----------------------------------------------------------------
#LANGUAGE      AMERICAN
#TERRITORY     AMERICA
#CHARACTER SET AL32UTF8

# You must set this to see Amerian and Russian unicode
# Mac lion side; put on debian64lrg as well.
#N LS_LANG=RUSSIAN_CIS.AL32UTF8  # see if format is aligned-- no change, misaligned
NLS_LANG=AMERICAN_AMERICA.AL32UTF8

export LESS="-erX"
export CLICOLOR=1
export TERM=xterm-color
export DART_HOME
export LEIN_HOME
export CLASSPATH
export USSR
export JVM_ARGS="-Xms1024m -Xmx1024m"
export JAVA_OPTS=-Dcom.sun.management.jmxremote
export GRAILS_OPTS="-Xmx1G -Xms256m -XX:MaxPermSize=256m"
export MAVEN_OPTS='-Xmx1024m -XX:MaxPermSize=512m'
export PYALGOR
export SPRINGROO
export ROO_HOME
export GRIFFON_HOME
export GRAILS
export GROOVY_HOME
export MONGODB
export GIT
export MORPH
export PROLOGTREE
export JMETER_HOME
export H2
export SCALA_HOME
export MD
export H2
export AS_INSTALL_PARENT
export AS_INSTALL
export ANT
export JAVA_HOME
export DERBY_HOME
export MERCURYPROGRAMS
export APACHE
export DYLD_LIBRARY_PATH
export TNS_ADMIN
export ORACLE_SID
export NLS_LANG
export ORACLE_PATH=~/sqlScripts  #sql>@cs1.sql  ;; works
export SQLPATH=~/oraLogin #sql> ;; prompt define, also _editor
export trdCodePath          # .m files
export stocksCvsPath        # original prices from Yahoo
export stocksFilterCvsPath  # Same, but with first line, last column deleted
export dbsqlPath
export dfsLst
export VISUAL
export PATH
export MANPATH
export JAVA_HOME
export SUBVERSION
export LC_CTYPE=en_US.UTF-8


# take out for now, since I want normal Mac's ruby
# Don't bother with rvm
#[[ -s "$HOME/.rvm/scripts/rvm" ]] && . "$HOME/.rvm/scripts/rvm"
#[[ -r $HOME/.rvm/scripts/completion ]] && . $HOME/.rvm/scripts/completion
