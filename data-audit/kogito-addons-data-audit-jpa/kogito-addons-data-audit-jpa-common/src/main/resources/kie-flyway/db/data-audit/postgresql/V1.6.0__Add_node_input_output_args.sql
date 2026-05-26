-- Add input_args and output_args columns to Process_Instance_Node_Log table
ALTER TABLE Process_Instance_Node_Log ADD COLUMN input_args VARCHAR(4000);
ALTER TABLE Process_Instance_Node_Log ADD COLUMN output_args VARCHAR(4000);
