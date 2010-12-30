#This archive turns a well formed strategy into a ff compiled file.
#remove spaces
sed 's/ //g' $1 > $1'.temp'
#convert new lines to unix format
#sed 's/.$//' $1'.temp' >$1'.ff'
#remove empty lines
#sed '/^$/d' $1'.ff' > $1'.temp'
#change \n?
#compile commands
sed 's/correrSinBalon/o/g' $1'.temp' > $1'.ff'
sed 's/tirar/t/g' $1'.ff' > $1'.temp'
sed 's/pasar/p/g' $1'.temp' > $1'.ff'
sed 's/andarConBalon/n/g' $1'.ff' > $1'.temp'
#compile directions
sed 's/adelante/f/g' $1'.temp' > $1'.ff'
sed 's/atras/v/g' $1'.ff' > $1'.temp'
sed 's/derecha/r/g' $1'.temp' > $1'.ff'
sed 's/izquierda/l/g' $1'.ff' > $1'.temp'
#remove parentesis
sed 's/)//g' $1'.temp' > $1'.ff'
sed 's/(/,/g' $1'.ff' > $1'.temp'
#sed 's/,//g' $1'.ff' > $1'.temp'
#fill with blank parameters
#sed 's/t/txx/g' $1'.ff' > $1'.temp'
#sed 's/p./&x/g' $1'.temp' > $1'.ff'
#sed 's/n./&x/g' $1'.ff' > $1'.temp'

#end in .ff
cp $1'.temp' $1'.ff'
rm $1'.temp'