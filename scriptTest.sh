# Navigate into the directory with the src file

# Compile all java files
javac -cp . src/sample/*.java

# Navigate into sample package
cd src

# Run Main class file
java -cp . sample.Main

# NOTE: If there is a "Permission denied error", do following:
# cd to where this script is stored
# enter this into terminal --> chmod 755 scriptTest.sh
# rerun script --> ./script_name.sh