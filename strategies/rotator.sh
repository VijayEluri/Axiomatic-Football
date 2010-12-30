#This script rotates a ff compiled file or a position file.
#rotate directions
sed 's/f/q/g' $1 > $1'.fr'
sed 's/v/y/g' $1'.fr' > $1'.temp'
sed 's/r/ñ/g' $1'.temp' > $1'.fr'
sed 's/l/z/g' $1'.fr' > $1'.temp'

sed 's/q/v/g' $1'.temp' > $1'.fr'
sed 's/y/f/g' $1'.fr' > $1'.temp'
sed 's/ñ/l/g' $1'.temp' > $1'.fr'
sed 's/z/r/g' $1'.fr' > $1'.temp'

#rotate coordinates
#x: 0->2 1->1 2->0
sed 's/0,\(.\)/9,\1/g' $1'.temp' > $1'.fr'
sed 's/2,\(.\)/8,\1/g' $1'.fr' > $1'.temp'

sed 's/9,\(.\)/2,\1/g' $1'.temp' > $1'.fr'
sed 's/8,\(.\)/0,\1/g' $1'.fr' > $1'.temp'

#y: 0->4 1->3 2->2 3->1 4->0
sed 's/\(.\),0/\1,9/g' $1'.temp' > $1'.fr'
sed 's/\(.\),1/\1,8/g' $1'.fr' > $1'.temp'
sed 's/\(.\),3/\1,7/g' $1'.temp' > $1'.fr'
sed 's/\(.\),4/\1,6/g' $1'.fr' > $1'.temp'

sed 's/\(.\),9/\1,4/g' $1'.temp' > $1'.fr'
sed 's/\(.\),8/\1,3/g' $1'.fr' > $1'.temp'
sed 's/\(.\),7/\1,1/g' $1'.temp' > $1'.fr'
sed 's/\(.\),6/\1,0/g' $1'.fr' > $1'.temp'

cp $1'.temp' $1'.fr'
rm $1'.temp'