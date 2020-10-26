# Playlist Processor
## Usage
playlist.py input_json_file change_file output_json_file

## Change file
Format: JSON
```json
{
  "changes" : [
    {
      "operation": "// operation type - AddOrPatch or Delete"
      "details": { "// same format as playlist definition"
        "id" : "4",
        "user_id" : "7",
        "song_ids" : [
            "8",
            "32",
            "15",
            "16"
        ]
      }
    },
  ...... more changes ......
  ]
}
```
## Scalability
### Handling large input file
For large json file, we can not load it into memory and need to process the file stream by stream. Here are the details
1. Process objects from input file
In this case, we need to keep a stack to keep track of the object in the current level.
Stack when reaching the first user's 'name' property -> [[root] [users] [user1] ['name']]
After all the properties of this user is processed, user1 is popped out and stack becomes -> [[root] [users]]
2. Store the data
Instead of persisting the playlist data in the memory, we write the playlists in a text file and each playlist takes a whole line
We also need to persist an index file mapping playlist id to the line number of the playlist in the file above for fast querying.
For deleted playlists, we don't remove it from the file. Instead we mark it as removed. Either in the index file or data file is fine.

### Handling large change file
For large change file, we also need to process the file stream by stream. Strategy is same as the large input file(using stack). The only difference is we don't need to store the changes.
