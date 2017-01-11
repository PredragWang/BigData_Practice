import sys
import re
import json
from util import sentiment_scores, parse_text, parse_obj_post, get_us_states

def parse_tweets_file(tweet_file, sent_file):
  scores = sentiment_scores(sent_file)
  score_by_state = {}
  for line in tweet_file:
    obj = json.JSONDecoder().decode(line)
    if 'created_at' in obj.keys(): # then this is a tweet
      state = get_us_states(obj)
      if not state: continue
      terms = parse_obj_post(obj)
      score = 0
      for term in terms:
        term_score = scores.get(term.lower())
        score += (0 if term_score==None else term_score)
      if state not in score_by_state:
        score_by_state[state] = score
      else: score_by_state[state] += score
  ret = sorted(score_by_state.items(), key=lambda x:x[1], reverse=True)
  if len(ret) > 0:
    print ret[0][0]

def lines(fp):
  print str(len(fp.readlines()))

def main():
  sent_file = open(sys.argv[1])
  tweet_file = open(sys.argv[2])
  parse_tweets_file(tweet_file, sent_file)

if __name__ == '__main__':
  main()
