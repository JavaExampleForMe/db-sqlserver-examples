use [idatest]
GO
CREATE TABLE [dbo].[TestStreaming]([Id] [int] NULL) ON [PRIMARY]

GO


INSERT INTO [TestStreaming]([Id]) VALUES (1)
GO

DECLARE @TopToTake INT =0;
 WHILE (SELECT COUNT(1) FROM [TestStreaming]) < 2000
  BEGIN
    INSERT [TestStreaming]
    SELECT Id + (SELECT COUNT(1) FROM [TestStreaming])
    FROM [TestStreaming]
  END




SELECT * FROM  [TestStreaming] ORDER BY Id
