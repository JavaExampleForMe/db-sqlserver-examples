USE [myTestDB]
GO
IF NOT EXISTS (SELECT *
           FROM sys.objects
           WHERE type='U' AND object_id = OBJECT_ID(N'[dbo].[User]'))
BEGIN
    PRINT ('creating table [User]')
    CREATE TABLE [dbo].[User](
        [ID]                    [int]  IDENTITY(1,1)          NOT NULL,
        [Name]                [NVARCHAR](20)            NOT NULL,
     CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED ([ID] ASC)
    )
END
GO
