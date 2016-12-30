#!/usr/bin/python
import sys


# Input: <Matrix, x, y, value, L>
# z is the number of times Matrix[x,y] will be used
# for the first matrix, L = <number of columns in output array>
# for the second matrix, L = <number of rows in the output array>
# Output: 
# Key = <i,k,j>, Value = v
# <i,k> = the index of the output array,
# j = the number to be used in the jth multiplication
# there will be exactly two values for each key=<i,k,j>

for line in sys.stdin:
  (matrix, x, y, v, L) = line.split(",")
  if matrix == "A":
    for z in range(int(L)):
      print "%s,%d,%s\t%s" % (x, z, y, v)
  if matrix == "B":
    for z in range(int(L)):
      print "%d,%s,%s\t%s" % (z, y, x, v)
