## Client Messages
Note: Messages will be minified, whitespace is for readability only
<table>
<tr>
<th>Message Type</th>
<th>Sub-Message Type</th>
<th>Example</th>
<th>Notes</th>
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
<td>HostServerMessage</td>
<td>StartTrivia</td>
<td>
<pre>
{
  "messageType": "HostServerMessage",
  "payload": {
    "messageType": "StartTrivia"
  }
}
</pre>
</td>
</tr>
<tr>
<td>HostServerMessage</td>
<td>Next</td>
<td>
<pre>
{
  "messageType": "HostServerMessage",
  "payload": {
    "messageType": "Next"
  }
}
</pre>
</td>
<td>Transitions game to next "slide"</td>
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
<td>
Server responds with TableStatusResponse message
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
<td>
Server responds with CreateTeamResponse message
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
<td>
Server responds with JoinTeamResponse message
</td>
</tr>
<tr>
<td>PlayerServerMessage</td>
<td>AnswerQuestion</td>
<td>
<pre>
{
  "messageType": "ServerPlayerMessage",
  "payload": {
    "messageType": "AnswerSubmission",
    "payload": {
      "roundNumber": 1,
      "questionNumber": 1,
      "answer": "question answer"
    }
  }
}
</pre>
</td>
<td>
Answers submitted will be sent to team leader for review.
</td>
</tr>
<tr>
<td>PlayerServerMessage</td>
<td>FinalAnswer</td>
<td>
<pre>
{
  "messageType": "PlayerServerMessage",
  "payload": {
    "messageType": "FinalAnswer",
    "payload": {
      "roundNumber": 1,
      "questionNumber": 2,
      "answer": "question answer"
    }
  }
}
</pre>
</td>
<td>
Only the team leader can submit a FinalAnswer.
</td>
</tr>
<tr>
<td>PlayerServerMessage</td>
<td>GradeQuestion</td>
<td>
<pre>
{
  "messageType": "PlayerServerMessage",
  "payload": {
    "messageType": "GradeQuestion",
    "payload": {
      "questionNumber": 1,
      "roundNumber": 0,
      "teamGrades": [{
        "teamId": 1,
        "correct": false
      },{
        "teamId": 5,
        "correct": true
      }]
    }
  }
}
</pre>
</td>
<td>
Only the team leader can submit grades.
</td>
</tr>
</table>

## Server Messages

<table>
<tr>
<th>Message Type</th>
<th>Sub-Message Type</th>
<th>Example</th>
<th>Notes</th>
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
<td>
When user connects they receive an array of all messages. Receive array with single message when a new message is sent.
</td>
</tr>
<tr>
<td>ServerHostMessage</td>
<td>TriviaStart</td>
<td>
<pre>
{
  "messageType": "ServerHostMessage",
  "payload": {
    "messageType": "TriviaStart",
    "payload": {
      "id": 1,
      "date": "2018-12-25T05:00:00.000Z",
      "hostName": "Trivia Master",
      "gameName": "ChampTrivia",
      "title": "Ultimate Trivia of Champions",
      "text": "The Greatest Trivia Ever",
      "image": "base64 webp image data",
      "completed": false
    }
  }
}
</pre>
</td>
</tr>
<tr>
<td>ServerHostMessage</td>
<td>RoundStart</td>
<td>
<pre>
{
  "messageType": "ServerHostMessage",
  "payload": {
    "messageType": "RoundStart",
    "payload": {
      "id": 2,
      "roundNumber": 1,
      "title": "Weird Al",
      "text": "Name the original song that Weird Al Parodied",
      "image": "base64 webp image data"
    }
  }
}
</pre>
</td>
</tr>
<tr>
<td>ServerHostMessage</td>
<td>Question</td>
<td>
<pre>
{
  "messageType": "ServerHostMessage",
  "payload": {
    "messageType": "Question",
    "payload": {
      "id": 3,
      "roundNumber": 0,
      "questionNumber": 1,
      "title": "Smells Like Nirvana",
      "text": "Released 1992",
      "image": "base64 webp image data"
    }
  }
}
</pre>
</td>
</tr>
<tr>
<td>ServerHostMessage</td>
<td>AnswerStatus</td>
<td>
<pre>
{
  "messageType": "ServerHostMessage",
  "payload": {
    "messageType": "AnswerStatus",
    "payload": {
    "roundNumber": 0,
      "questionNumber": 1,
      "numTeams": 3,
    "answersSubmitted": 2
    }
  }
}
</pre>
</td>
</tr>
<tr>
<td>ServerHostMessage</td>
<td>Grading</td>
<td>
<pre>
{
  "messageType": "ServerHostMessage",
  "payload": {
    "messageType": "Grading",
    "payload": {
      "id": 3,
      "roundNumber": 0,
      "questionNumber": 1,
      "title": "Smells Like Nirvana",
      "text": "Released 1992",
      "image": "base64 webp image data",
      "answer": "Smells Like Teen Spirit"
    }
  }
}
</pre>
</td>
</tr>
<tr>
<td>ServerHostMessage</td>
<td>GradingStatus</td>
<td>
<pre>
{
  "messageType": "ServerHostMessage",
  "payload": {
    "messageType": "GradingStatus",
    "payload": {
    "roundNumber": 0,
    "questionNumber": 1,
    "numTeams": 3,
    "gradesSubmitted": 2
    }
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
    "status": "closed | open | active",
    "game": {
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
<td>
"game" section of payload is omitted if there is status closed
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
      "team": {
        "teamName": "This is a team Name",
        "teamLeader": "keshek"
      }
    }
  }
}
</pre>
</td>
<td>
"team" section of payload is omitted if status is "no team"
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
<td>
"teamName" section of payload is omitted if success is false and reason is omitted if success is true
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
<td>
"teamName" section of payload is omitted if success is false and reason is omitted if success is true
</td>
</tr>
<tr>
<td>ServerPlayerMessage</td>
<td>TriviaStart</td>
<td>
<pre>
{
  "messageType": "ServerPlayerMessage",
  "payload": {
    "messageType": "TriviaStart",
    "payload": {
      "id": 1,
      "date": "2018-12-25T05:00:00.000Z",
      "hostName": "Trivia Master",
      "gameName": "ChampTrivia",
      "title": "Ultimate Trivia of Champions",
      "text": "The Greatest Trivia Ever",
      "image": "base64 webp image data",
      "completed": false
    }
  }
}
</pre>
</td>
</tr>
<tr>
<td>ServerPlayerMessage</td>
<td>RoundStart</td>
<td>
<pre>
{
  "messageType": "ServerPlayerMessage",
  "payload": {
    "messageType": "RoundStart",
    "payload": {
      "id": 2,
      "roundNumber": 1,
      "title": "Weird Al",
      "text": "Name the original song that Weird Al Parodied",
      "image": "base64 webp image data"
    }
  }
}
</pre>
</td>
</tr>
<tr>
<td>ServerPlayerMessage</td>
<td>Question</td>
<td>
<pre>
{
  "messageType": "ServerPlayerMessage",
  "payload": {
    "messageType": "Question",
    "payload": {
      "id": 3,
      "questionNumber": 1,
      "title": "Smells Like Nirvana",
      "text": "Released 1992",
      "image": "base64 webp image data"
    }
  }
}
</pre>
</td>
</tr>
<tr>
<td>ServerPlayerMessage</td>
<td>AnswerSubmission</td>
<td>
<pre>
{
  "messageType": "ServerPlayerMessage",
  "payload": {
    "messageType": "AnswerSubmission",
    "payload": {
      "roundNumber": 1,
      "questionNumber": 2,
      "answer": "question answer",
    }
  }
}
</pre>
</td>
<td>
Sent to team leader whenever a team member (including the team leader) submits an answer.
</td>
</tr>
<tr>
<td>ServerPlayerMessage</td>
<td>FinalAnswerResponse</td>
<td>
<pre>
{
  "messageType": "ServerPlayerMessage",
  "payload": {
    "messageType": "FinalAnswerResponse",
    "payload": {
      "roundNumber": 1,
      "questionNumber": 2,
      "answer": "question answer",
      "success": true | false,
      "reason": "Submitter, not team leader" | "Team already answered"
    }
  }
}
</pre>
</td>
<td>
If success is false, message is sent to only submitter and reason is included. If success is true, message is sent to all team members and reason is not included.
</td>
</tr>
<tr>
<td>ServerPlayerMessage</td>
<td>Grading</td>
<td>
<pre>
{
  "messageType": "ServerPlayerMessage",
  "payload": {
    "messageType": "Grading",
    "payload": {
      "question": {
        "id": 3,
        "questionNumber": 1,
        "roundNumber": 0,
        "title": "Smells Like Nirvana",
        "text": "Released 1992",
        "image": "base64 webp image data",
        "answer": "Smells Like Teen Spirit"
      },
      "teamAnswers": [{
        "teamId": 1,
        "answer": "Come as you are"
      },{
        "teamId": 5,
        "answer": "Smells Like Teen Spirit"
      }]
    }
  }
}
</pre>
</td>
</tr>
</table>