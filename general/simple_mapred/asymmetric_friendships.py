import MapReduce
import sys

"""
Asymmetric friends Example in the Simple Python MapReduce Framework
"""

mr = MapReduce.MapReduce()

# =============================
# Do not modify above this line

def mapper(record):
    key = record[0]
    value = record[1]
    if key < value:
        pair = key + ' ' + value
        cnt = 1
    else:
        pair = value + ' ' + key
        cnt = -1
    mr.emit_intermediate(pair, cnt)

def reducer(key, list_of_values):
    cnt = 0
    for v in list_of_values:
      cnt += v
    pair = key.split()
    p1 = pair[0]
    p2 = pair[1]
    if cnt != 0:
        mr.emit((p1, p2))
        mr.emit((p2, p1))

# Do not modify below this line
# =============================
if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
