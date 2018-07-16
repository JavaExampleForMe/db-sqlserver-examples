CREATE FUNCTION [dbo].[fn_intlist_to_tbl] (@list nvarchar(MAX))
   RETURNS @tbl TABLE (listpos int IDENTITY(1, 1) NOT NULL,
                       number  bigint NOT NULL) AS
BEGIN
   DECLARE @startpos int,
           @endpos   int,
           @textpos  int,
           @chunklen smallint,
           @str      nvarchar(4000),
           @tmpstr   nvarchar(4000),
           @leftover nvarchar(4000)
   SET @textpos = 1
   SET @leftover = ''
   WHILE @textpos <= datalength(@list) / 2
   BEGIN
      SET @chunklen = 4000 - datalength(@leftover) / 2
      SET @tmpstr = ltrim(@leftover +
                    substring(@list, @textpos, @chunklen))
      SET @textpos = @textpos + @chunklen
      SET @startpos = 0
      SET @endpos = charindex(',' , @tmpstr)
      WHILE @endpos > 0
      BEGIN
         SET @str = substring(@tmpstr, @startpos + 1,
                              @endpos - @startpos - 1)
         IF @str <> ''
            INSERT @tbl (number) VALUES(convert(bigint, @str))
         SET @startpos = @endpos
         SET @endpos = charindex(',' ,
                                 @tmpstr, @startpos + 1)
      END
      SET @leftover = right(@tmpstr, datalength(@tmpstr) / 2 - @startpos)
   END
   IF ltrim(rtrim(@leftover)) <> ''
      INSERT @tbl (number) VALUES(convert(bigint, @leftover))
   RETURN
END
----Here is an example on how you would use this function:
--CREATE PROCEDURE get_product_names_iter @ids varchar(50) AS
--   SELECT P.Name, P.ProductID
--   FROM   AdventureWorks2012.Production.Product P
--   JOIN   fn_intlist_to_tbl(@ids) i ON P.ProductID = i.number
--GO
GO

CREATE TABLE [dbo].[SearchTask](
	[SearchTaskId] [int] IDENTITY(1,1) NOT NULL,
	[SourceRequestId] [int] NOT NULL,
	[SearchTaskDefinition] [nvarchar](max) NOT NULL,
	[SearchTaskStatusId] [int] NOT NULL,
	[SearchTaskProgress] [int] NOT NULL,
	[CreationDate] [datetime] NOT NULL,
	[ModifyDate] [datetime] NOT NULL,
 CONSTRAINT [PK_SearchTask] PRIMARY KEY CLUSTERED([SearchTaskId] ASC))
GO

CREATE TABLE [dbo].[SearchTaskBatch](
	[BatchId] [int] IDENTITY(1,1) NOT NULL,
	[SearchTaskId] [int] NOT NULL,
	[SourceRequestId] [int] NOT NULL,
	[BatchStatusId] [int] NOT NULL,
	[BatchResultsNumRows] [int] NOT NULL,
	[BatchQueryDefinition] [nvarchar](max) NOT NULL,
	[CreationDate] [datetime] NOT NULL,
	[ModifyDate] [datetime] NOT NULL,
 CONSTRAINT [PK_SearchTaskBatch] PRIMARY KEY CLUSTERED([BatchId] ASC))
GO

CREATE TABLE [dbo].[SearchResults](
	[SourceRequestId] [int] NOT NULL,
	[SearchTaskId] [int] NOT NULL,
	[BatchId] [int] NOT NULL,
	[iInteractionID] [bigint] NOT NULL,
	[dtContactGMTStartTime] [datetime] NOT NULL,
	[iInitiatorUserID] [int] NOT NULL,
	[iArchiveID] [bigint] NOT NULL,
	[CreationDate] [datetime] NOT NULL,
	[ModifyDate] [datetime] NOT NULL
)
GO

CREATE VIEW vwProduceResults AS
SELECT 1 iInteractionID, 1 iArchiveID, 101 iInitiatorUserID ,CONVERT(DATETIME,'2012-07-18 13:27:18') dtContactGMTStartTime
UNION ALL
SELECT 2 iInteractionID, 2 iArchiveID, 102 iInitiatorUserID ,CONVERT(DATETIME,'2011-01-11 13:11:18') dtContactGMTStartTime
GO 
