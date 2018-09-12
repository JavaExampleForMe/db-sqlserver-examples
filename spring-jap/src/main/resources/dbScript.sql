CREATE TABLE [dbo].[Policy](
	[policyId] [int] NOT NULL IDENTITY,
	[mediaTypes] [int] NOT NULL,
	[deletionTypes] [int] NOT NULL,
	[creationDate] [datetime] NOT NULL,
	[modifyDate] [datetime] NOT NULL,
 CONSTRAINT [PK_Policy] PRIMARY KEY CLUSTERED
(
	[policyId] ASC
))
GO

ALTER TABLE [dbo].[Policy] ADD  CONSTRAINT [DF_Policy_modifyDate]  DEFAULT (getutcdate()) FOR [modifyDate]
GO


CREATE TABLE [dbo].[Line](
	[policyId] [int] NOT NULL,
	[lineId] [int] NOT NULL,
	[lineStatus] [tinyint] NOT NULL,
	[creationDate] [datetime] NOT NULL,
	[modifyDate] [datetime] NOT NULL,
 CONSTRAINT [PK_Line] PRIMARY KEY CLUSTERED
(
	[policyId] ASC,
	[lineId] ASC
))
GO

ALTER TABLE [dbo].[Line] ADD  CONSTRAINT [DF_Line_creationDate]  DEFAULT (getutcdate()) FOR [creationDate]
GO

ALTER TABLE [dbo].[Line] ADD  CONSTRAINT [DF_Job_modifyDate]  DEFAULT (getutcdate()) FOR [modifyDate]
GO

ALTER TABLE [dbo].[Line]  WITH CHECK ADD  CONSTRAINT [FK_Job_Policy] FOREIGN KEY([policyId])
REFERENCES [dbo].[Policy] ([policyId])
ON DELETE CASCADE
GO

