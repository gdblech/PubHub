## Client Messages
Note: Messages will be minified, whitespace is for readability only
<table>
<tr>
<th>Message Type</th>
<th>Sub-Message Type</th>
<th>Example</th>
</tr>
<tr>
<td>ClientChatMessage</td>
<td></td>
<td>
<pre>
{
	"messageType":"ClientServerChatMessage",
	"payload": {
		"message":"eat my shorts"
	}
}
</pre>
</td>
</tr>
<tr>
<td>HostServerMessage</td>
<td>OpenGame</td>
<td>
<pre>
{
    "messageType": "HostServerMessage",
    "payload": {
        "messageType": "OpenGame",
        "payload": {
            "gameId": 1
        }
    }
}
</pre>
</td>
</tr>
<tr>
<td>HostServerMessage</td>
<td>EndGame</td>
<td>
<pre>
{
    "messageType": "HostServerMessage",
    "payload": {
        "messageType": "EndGame"
    }
}
</pre>
</td>
</tr>
<tr>
<td>PlayerServerMessage</td>
<td>TableStatusRequest</td>
<td>
<pre>
{
	"messageType": "PlayerServerMessage",
	"payload": {
		"messageType": "TableStatusRequest",
		"payload": {
			"QRCode": "1539ccd0-e391-11e8-9f32-f2801f1b9fd1"
		}
	}
}
</pre>
</td>
</tr>
<tr>
<td>PlayerServerMessage</td>
<td>CreateTeam</td>
<td>
<pre>
{
	"messageType": "PlayerServerMessage",
	"payload": {
		"messageType": "CreateTeam",
		"payload": {
			"QRCode": "1539ccd0-e391-11e8-9f32-f2801f1b9fd",
			"teamName": "This is a team name"
		}
	}
}
</pre>
</td>
</tr>
<tr>
<td>PlayerServerMessage</td>
<td>JoinTeam</td>
<td>
<pre>
{
	"messageType": "PlayerServerMessage",
	"payload": {
		"messageType": "JoinTeam",
		"payload": {
			"QRCode": "1539ccd0-e391-11e8-9f32-f2801f1b9fd"
		}
	}
}
</pre>
</td>
</tr>
</table>

## Server Messages

<table>
<tr>
<th>Message Type</th>
<th>Sub-Message Type</th>
<th>Example</th>
</tr>
<tr>
<td>ServerChatMessage</td>
<td></td>
<td>
<pre>
{
  "messageType": "ServerClientChatMessage",
  "payload": {
    "messages": [
      {
        "message": "eat my shorts",
        "timestamp": "2018-10-14T16:36:35.000Z",
        "user": "tcox3799"
      },
      {
        "message": "heyo",
        "timestamp": "2018-10-14T16:38:22.000Z",
        "user": "keshek"
      }
    ]
  }
}
</pre>
</td>
</tr>
<tr>
<td>ServerPlayerMessage</td>
<td>GameInfo</td>
<td>
<pre>
{
  "messageType": "ServerPlayerMessage",
  "payload": {
    "messageType": "GameInfo",
    "payload": {
      "status": "open | closed",
      "game": { 	// omitted if game is not open
        "title": "Really cool trivia",
        "text": "This is a trivia",
        "image": "ajfhldjfdhfljkadhfjkahd",
        "started": false,
        "grading": false
      }
    }
  }
}
</pre>
</td>
</tr>
<tr>
<td>ServerPlayerMessage</td>
<td>TableStatusResponse</td>
<td>
<pre>
{
	"messageType": "ServerPlayerMessage",
	"payload": {
		"messageType": "TableStatusResponse",
		"payload": {
			"QRCode": "1539ccd0-e391-11e8-9f32-f2801f1b9fd",
			"status": "no team | team open | team full",
			"team": { 	// omitted if no team
				"teamName": "This is a team Name",
				"teamLeader": "keshek"
			}
		}
	}
}
</pre>
</td>
</tr>
<tr>
<td>ServerPlayerMessage</td>
<td>CreateTeamResponse</td>
<td>
<pre>
{
	"messageType": "ServerPlayerMessage",
	"payload": {
		"messageType": "CreateTeamResponse",
		"payload": {
			"success": true | false,
			"QRCode": "1539ccd0-e391-11e8-9f32-f2801f1b9fd",
			"teamName": "This is a team name",
			"reason": "Team already exists for table" | "User already belongs to a team"  // omitted if successful
		}
	}
}
</pre>
</td>
</tr>
<tr>
<td>ServerPlayerMessage</td>
<td>JoinTeamResponse</td>
<td>
<pre>
{
	"messageType": "ServerPlayerMessage",
	"payload": {
		"messageType": "JoinTeamResponse",
		"payload": {
			"QRCode": "1539c834-e391-11e8-9f32-f2801f1b9fd1",
			"teamName": "Team Awesome",
			"success": true | false,
			"reason": "User already belongs to a team" | "No matching team found for table" // omitted if successful
		}
	}
}
</pre>
</td>
</tr>
</table>