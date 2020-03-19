USE [master]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF  EXISTS (SELECT * FROM sys.databases WHERE name = N'myTestDB')
    BEGIN
        ALTER DATABASE myTestDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
        PRINT ('dropping database [myTestDB]')
        DROP DATABASE [myTestDB]
    END;
GO

PRINT ('creating database [myTestDB]')
CREATE DATABASE [myTestDB]
GO


