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
	SET @OutCounter = @InCounter
	SELECT * FROM @tblA
END
GO
