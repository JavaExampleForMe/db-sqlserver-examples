USE [idatest]
GO

CREATE TABLE [dbo].[Reservation](
	[reservationId] [int] NOT NULL IDENTITY,
	[mediaTypes] [int] NOT NULL,
	[orderTypes] [int] NOT NULL,
	[creationDate] [datetime] NOT NULL,
	[modifyDate] [datetime] NOT NULL,
 CONSTRAINT [PK_Reservation] PRIMARY KEY CLUSTERED
(
	[reservationId] ASC
))
GO

ALTER TABLE [dbo].[Reservation] ADD  CONSTRAINT [DF_Reservation_modifyDate]  DEFAULT (getutcdate()) FOR [modifyDate]
GO


CREATE TABLE [dbo].[Line](
	[reservationId] [int] NOT NULL,
	[lineId] [int] NOT NULL,
	[lineStatus] [tinyint] NOT NULL,
	[color] [varchar](50)  NULL,
	[model] [varchar](50)  NULL,
	[creationDate] [datetime] NOT NULL,
	[modifyDate] [datetime] NOT NULL,
 CONSTRAINT [PK_Line] PRIMARY KEY CLUSTERED
(
	[reservationId] ASC,
	[lineId] ASC
))
GO

ALTER TABLE [dbo].[Line] ADD  CONSTRAINT [DF_Line_creationDate]  DEFAULT (getutcdate()) FOR [creationDate]
GO

ALTER TABLE [dbo].[Line] ADD  CONSTRAINT [DF_Line_modifyDate]  DEFAULT (getutcdate()) FOR [modifyDate]
GO

ALTER TABLE [dbo].[Line]  WITH CHECK ADD  CONSTRAINT [FK_Line_Reservation] FOREIGN KEY([reservationId])
REFERENCES [dbo].[Reservation] ([reservationId])
ON DELETE CASCADE
GO

