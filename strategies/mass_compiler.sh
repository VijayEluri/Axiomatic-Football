#this file compiles and create rotated versions of every strategy under the strategies folder
for dir in $(find ./ -type d); do
	#Compile strategies
	./compiler.sh $dir/defensiva.txt
	./compiler.sh $dir/ofensiva.txt
	#Rotate strategies
	./rotator.sh $dir/defensiva.txt.ff
	./rotator.sh $dir/ofensiva.txt.ff
	#Compile initial positions
	./compiler.sh $dir/pdefensiva.txt
	./compiler.sh $dir/pofensiva.txt
	#Rotate initial positions
	./rotator.sh $dir/pofensiva.txt.ff
	./rotator.sh $dir/pdefensiva.txt.ff	
done
