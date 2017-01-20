import sys
import re
import json
from util import sentiment_scores, parse_text, parse_obj_post

def parse_tweets_file(tweet_file, sent_file):
  """
  parse json file of tweet data
  """
  scores = sentiment_scores(sent_file)
  tweets = []
  for line in tweet_file:
    obj = json.JSONDecoder().decode(line)
    if 'created_at' in obj.keys(): # then this is a tweet
      terms = parse_obj_post(obj)
      score = 0
      for term in terms:
        term_score = scores.get(term.lower())
        score += (0 if term_score==None else term_score)
      print score


def lines(fp):
  print str(len(fp.readlines()))

def main():
  sent_file = open(sys.argv[1])
  tweet_file = open(sys.argv[2])
  parse_tweets_file(tweet_file, sent_file)

if __name__ == '__main__':
  main()
