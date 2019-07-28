USE [idatest]
GO
CREATE TYPE [dbo].[tblAType] AS TABLE(
	[id] [int] NOT NULL,
	[name] [nvarchar](10) NOT NULL,
	[creationDateTime] [datetime] NULL
)
GO

CREATE PROCEDURE [dbo].[spToTest](
	@tblA         tblAType READONLY
	,@InCounter   INT
	,@OutCounter   INT OUTPUT
) AS
BEGIN
    PRINT 'SP has started'
	SET @OutCounter = @InCounter
	SELECT * FROM @tblA
    PRINT 'SP has ended'
END
GO

CREATE TABLE [dbo].[Parent](
	[parentId] [int] NOT NULL IDENTITY,
	[mediaTypes] [int] NOT NULL,
	[deletionTypes] [int] NOT NULL,
	[creationDate] [datetime] NOT NULL,
	[modifyDate] [datetime] NOT NULL,
 CONSTRAINT [PK_Parent] PRIMARY KEY CLUSTERED
(
	[parentId] ASC
))
GO

ALTER TABLE [dbo].[Parent] ADD  CONSTRAINT [DF_Parent_modifyDate]  DEFAULT (getutcdate()) FOR [modifyDate]
GO


CREATE TABLE [dbo].[Child](
	[parentId] [int] NOT NULL,
	[childId] [int] NOT NULL,
	[status] [tinyint] NOT NULL,
	[creationDate] [datetime] NOT NULL,
	[modifyDate] [datetime] NOT NULL,
 CONSTRAINT [PK_Child] PRIMARY KEY CLUSTERED
(
	[parentId] ASC,
	[childId] ASC
))
GO

ALTER TABLE [dbo].[Child] ADD  CONSTRAINT [DF_Child_creationDate]  DEFAULT (getutcdate()) FOR [creationDate]
GO

CREATE TABLE [dbo].[TestCsv](
	[id] [int] NOT NULL,
	[name] [nvarchar](100) NOT NULL,
 CONSTRAINT [PK_TestCsv] PRIMARY KEY CLUSTERED
(
	[id] ASC
))
GO

CREATE TABLE [dbo].[DatesTbl](
	[SearchTaskId] [int] NOT NULL,
	[dtContactGMTStartTime] [datetime] NOT NULL,
	[iArchiveID] [bigint] NOT NULL,
	[BatchId] [int] NOT NULL,
CONSTRAINT [PK_DatesTbl] PRIMARY KEY CLUSTERED
(
	[SearchTaskId] ASC,
	[dtContactGMTStartTime] ASC,
	[iArchiveID] ASC
))
GO
INSERT INTO [DatesTbl]
VALUES (1, '20020202 02:02:02.003', 1, 1),
		(1, '20020202 02:02:02.007', 1, 1)
GO


