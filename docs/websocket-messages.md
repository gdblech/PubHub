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
</td>
</pre>
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
</td>
</pre>
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
</td>
</pre>
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
</td>
</pre>
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
</td>
</pre>
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
</td>
</pre>
</tr>
</table>

## Server MEssages

| Message Type | Sub-Message Type | Example Message |
|---|---|---|
|