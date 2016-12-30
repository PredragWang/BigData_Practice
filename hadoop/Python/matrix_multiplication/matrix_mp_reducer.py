#!/usr/bin/python

import sys

# Process the output of the temporary reducer

pKey = None
sum_v = 0
na = False

def output():
  if na == True:
    print "%s\tNA" % (pKey)
  else:
    print "%s\t%d" % (pKey, sum_v)

for line in sys.stdin:
  (key, value) = line.strip().split("\t")
  if key == None or value == None:
    continue
  if pKey == None:
    pKey = key
  elif pKey != key:
    output()
    pKey = key
    sum_v = 0
    na = False
  if na == True:
    continue
  try:
    sum_v += int(value)
  except ValueError:
    na = True

if pKey != None:
  output()
