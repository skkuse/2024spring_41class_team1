// PatternDialog.js
import React from 'react';
import { Dialog, DialogTitle, DialogContent, Typography } from "@mui/material";

function PatternDialog({ open, handleClose, pattern }) {
    return (
        <Dialog
            open={open}
            onClose={handleClose}
            scroll="paper"
            aria-labelledby="scroll-dialog-title"
            aria-describedby="scroll-dialog-description"
        >
            <DialogTitle id="scroll-dialog-title">{pattern ? `Pattern 설명: ${pattern.description}` : ''}</DialogTitle>
            <DialogContent dividers={true}>
                <Typography gutterBottom>
                    Code Before:
                </Typography>
                <Typography component="pre" style={{ background: "#f4f4f4", padding: "10px" }}>
                    {pattern ? pattern.before : ''}
                </Typography>
                <Typography gutterBottom>
                    Code After:
                </Typography>
                <Typography component="pre" style={{ background: "#f4f4f4", padding: "10px" }}>
                    {pattern ? pattern.after : ''}
                </Typography>
            </DialogContent>
        </Dialog>
    );
}

export default PatternDialog;
