#! /usr/bin/python

import sys
import re

err_msg = """
Usage: %s 
       <matrix_1_file>
       <matrix_1_column_count>
       <matrix_2_file>
       <matrix_2_column_count>

Matrix file format:
x11,x12,x13......x1n
x21,x22,x23......x2n
.
.
xm1,xm2,xm3......xmn
Note:No space allowd!!!
""" % (sys.argv[0])
if len(sys.argv) != 5:
  print err_msg 
  exit(1)

n1 = 0
n2 = 0

try:
  matrix1 = sys.argv[1]
  n1 = int(sys.argv[2])
  matrix2 = sys.argv[3]
  n2 = int(sys.argv[4])
except ValueError:
  print "Illegal args"

f = open("input.txt", "w");
  
try:
  f1 = open(sys.argv[1])
  # start processing matrix 1
  m1 = 0
  line = f1.readline()
  while line is not None:
    if len(line) == 0:
      break
    numbers = line.split(",")
    for i in range(n1):
      if i >= len(numbers):
        f.write("A,%d,%d,NA,%d\n" % (m1, i, n2))
      try:
        x = int(numbers[i])
        f.write("A,%d,%d,%d,%d\n" % (m1, i, x, n2))
      except ValueError:
        f.write("A,%d,%d,NA,%d\n" % (m1, i, n2))
    m1 += 1
    line = f1.readline()
  f1.close()
except IOError:
  print 'Can not open %s' % (sys.argv[1])

try:
  f2 = open(sys.argv[3])
  # start processing matrix 2
  m2 = 0
  line = f2.readline()
  while line is not None:
    if len(line) == 0:
      break
    numbers = line.split(",")
    for i in range(n2):
      if i >= len(numbers):
        f.write("B,%d,%d,NA,%d\n" % (m2, i, m1))
      try:
        x = int(numbers[i])
        f.write("B,%d,%d,%d,%d\n" % (m2, i, x, m1))
      except ValueError:
        f.write("B,%d,%d,NA,%d\n" % (m2, i, m1))
    m2 += 1
    if m2 == n1:
      break
    line = f2.readline()
  if m2 < n1:
    for m2 in range(m2, n1):
      f.write("B,%d,%d,NA\n")
  f2.close()
except IOError:
  print 'Can not open %s' % (sys.argv[3])
