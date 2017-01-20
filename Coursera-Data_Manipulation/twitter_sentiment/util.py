import sys
import re
import json

def sentiment_scores(sent_file):
  """
  Read sentiment score file
  Returns a dictionary with sentiment scores of all terms
  """
  scores = {}
  for line in sent_file:
    term, score = line.split("\t")
    scores[term] = int(score)
  return scores

def parse_text(text):
  """
  parse text
  Returns the terms in the text
  """
  if not text: return []
  text = text.encode("utf-8")
  terms = map(lambda x: x.lower(), re.compile("[a-zA-Z]\w+").findall(text))
  return terms

def parse_obj_all(obj):
  """
  Parse the text of the tweet recursively
  Return the terms in 'text' field of the tweet and all
  sub-objects
  """
  terms = []
  for k,v in obj.iteritems():
    if k == 'text':
      terms += parse_text(v)
    elif type(v) is dict:
      terms += parse_obj_all(v)
  return terms

def parse_obj_post(obj):
  """
  parse tweet object and get text of the post only
  """
  text = obj.get('text')
  return parse_text(text)

def hashtags(tweet):
  """
  Return list of hashtags in a tweet
  """
  if not tweet: return None
  if 'entities' in tweet:
    entities = tweet['entities']
    if 'hashtags' in entities:
      ht_list = entities['hashtags']
      ret = []
      for ht in ht_list:
        if 'text' in ht:
          ret.append(ht['text'].encode("utf-8"))
          return ret
  return None

def get_us_states(obj):
  """
  Get the 'State' of a tweet if posted in US
  """
  if 'place' not in obj: return None
  place = obj['place']
  if not place: return None
  if 'country_code' in place:
    if place['country_code'] != 'US': return None
    else:
      if 'full_name' in place:
        full_name = place['full_name'].split(",")
        if len(full_name) > 1:
          return full_name[-1].strip()
  return None

