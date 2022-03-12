# Calendar Phase 1

Ruotong Cheng, Hyung-Joo Michael Choi, Xiang Gao, Jiaqi Guo, and Mingjian Jiang

## What is this calendar program?

This calendar currently has TextUI that the user can interact with, the appearance of the TextUI is based on a command-line
system which is quite similar to Linux terminal. 

## How to use this calendar program?
- Once you start the program, you can login if you have already signed up, or you can sign up a new account by entering 
the username and associated password. A help menu will appear at the beginning, and you can pull up this help menu by typing in
`help`. Note that if you are logged in, your username will be displayed at left.
- Once you logged in, `help` can give you more options as what you can do. First thing that you should do is either create a 
new event or view existing events by `search` or `view`. Then, after requesting view or search, a list of events that match
your request will be pulled up. 
- These printed events will have a index of their own form 1-n. Next, you can select one of them by typing in the `select` and
appropriate number. Then details about selected event will be printed to the screen, and the user is able to add memo, tag, or alert
by using `add`.
- Once you have seen the details of the event, which includes memos/tags that also associated with a index, the user can input `delete`
to delete the specific memo, tag, or alert.
- You can also use `unselect` to unselect the current selected event, and then view some events and select another one.
- `jump` can help you to go to a future time or set the speed of time to see effect of alert. (Note that you can't travel back in time). Once it is the 
time of the alert, there will be a beep and a message displayed.

## Some Warnings and Cautions.
- **Very Important**! You need to `quit` or `logoff` before you want to exit the program in order to keep the data you created in the database.
Otherwise, it's not guaranteed that your data will be saved. If it happens, please delete the file username.ser(the account you logged in) under directory usr and accounts.ser.
- Also, if you entered a invalid input during a sequence of related commands, most likely you need to restart the process.
- Please input what the question asked you to input


## See Documentations
- [Javadoc](https://xiangtgao.github.io/)