import MapReduce
import sys

"""
Word Count Example in the Simple Python MapReduce Framework
"""

mr = MapReduce.MapReduce()

# =============================
# Do not modify above this line

def mapper(record):
    order_id = record[1]
    entry_type = 0 if record[0] == "order" else 1
    mr.emit_intermediate(order_id, [entry_type, record])

def reducer(key, list_of_values):
    n = len(list_of_values)
    list_of_values.sort()
    line_item_start = 0
    while list_of_values[line_item_start][0] == 0:
        line_item_start += 1
    if 0 < line_item_start < n:
        for o in range(line_item_start):
            for l in range(line_item_start, n):
                mr.emit(list_of_values[o][1]+list_of_values[l][1])

# Do not modify below this line
# =============================
if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
