#!/usr/bin/python

import sys

pKey = None
pValue = None

# Generate output with this format
# Input:
# Key = <i,k,j> (discussed in mapper script)
# Value = v 
# Output:
# key = <x, y>
# value = number needs to be summed into output[x, y]
# Just multiply the numbers with the same <i,k,j>

for line in sys.stdin:
  (key, value) = line.strip().split("\t")
  if key == None or value == None:
    continue
  if pKey == None or pKey != key:
    pKey = key
    pValue = value
  else:
    outputV = "NA"
    l_key = key.split(",")
    if len(l_key) == 3:
      try:
        outputV = int(pValue)*int(value)
      except ValueError:
        outputV = "NA"
      print "%s,%s\t%s" % (l_key[0], l_key[1], outputV)
    pKey = None
    pValue = None
