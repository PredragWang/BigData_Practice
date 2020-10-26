import sys
import json
from json import JSONEncoder
from collections import namedtuple
from types import SimpleNamespace as Namespace


class PlaylistDataHandler:
    def __init__(self, playListJson):
        '''
        Create Playlist Object from JSON file
        '''
        raw_json = open(playListJson).read()
        playListObj = json.loads(raw_json)
        self.playlists = {}
        self.songs = {}
        self.users = {}
        for pl in playListObj['playlists']:
            self.playlists[pl['id']] = pl
        for user in playListObj['users']:
            self.users[user['id']] = user
        for song in playListObj['songs']:
            self.songs[song['id']] = song
        
        print (self.users)
        print (self.playlists)

    def applyChange(self, change):
        '''
        Apply a change to playlist
        '''
        try:
            target_id = change['details']['id']
            operation = change['operation']
            if operation == "AddOrPatch":
                if target_id not in self.playlists:
                    self.playlists[target_id] = change['details']
                else:
                    print (self.playlists[target_id])
                    self.playlists[target_id]['song_ids'].extend(change['details']['song_ids'])
            elif operation == "Delete":
                if target_id in self.playlists:
                    del self.playlists[target_id]
        except:
            print ('Invalid change object format')
            raise

    def applyChanges(self, changes):
        '''
        Apply list of changes to the data
        '''
        for change in changes:
            self.applyChange(change)

    def output(self, outputFile):
        '''
        Write the playlist to json file
        '''
        playlistObj = {'users': list(self.users.values()),
                       'playlists': list(self.playlists.values()),
                       'songs': list(self.songs.values())}

        playListJson = json.dumps(playlistObj, indent=2)
        fw = open(outputFile, "w")
        fw.write(playListJson)
        fw.close()
        

def loadChangelist(changelist_json):
    '''
    Load change file
    '''
    raw_json = open(changelist_json).read()
    changelist_obj = json.loads(raw_json)
    return changelist_obj['changes']


def main(): 
    if len(sys.argv) != 4:
        print ("Usage: python playlist.py <input json> <change json> <output json>")
        return
    dataHandler = PlaylistDataHandler(sys.argv[1])
    changes = loadChangelist(sys.argv[2])
    dataHandler.applyChanges(changes)
    dataHandler.output(sys.argv[3])
  
   
if __name__=="__main__": 
    main() 

        
